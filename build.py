import os
import threading

threads = []

def build_application(app):
    threads.append(app)
    print("Building application {}".format(app))
    os.system("cd {} && ./gradlew build".format(app))
    print("Application {} finished building!".format(app))
    threads.remove(app)


def docker_compose_up():
    print("Running containers!")
    os.popen("docker-compose up --build -d").read()
    print("Pipeline finished!")


def build_all_applications():
    print("Starting application build!")
    build_application("stateful-auth-api")
    build_application("stateful-any-api")
    print("\n")


def remove_containers():
    print("Removing containers.")
    os.system("docker-compose down")


if __name__ == "__main__":
    print("Pipeline started!")
    build_all_applications()
    while len(threads) > 0:
        pass
    remove_containers()
    threading.Thread(target=docker_compose_up).start()
