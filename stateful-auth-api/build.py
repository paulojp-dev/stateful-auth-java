import os
import threading

threads = []

def build_application(app):
    threads.append(app)
    print("Building application {}".format(app))
    os.system("./gradlew build".format(app))
    print("Application {} finished building!".format(app))
    threads.remove(app)


def docker_compose_up():
    print("Running containers!")
    os.popen("docker-compose up --build -d").read()
    print("Pipeline finished!")


def build_all_applications():
    print("Starting application build!")
    threading.Thread(target=build_application,
                     args={"stateful-auth-api"}).start()


def remove_containers():
    print("Removing containers.")
    os.system("docker-compose down")
    containers = os.popen('docker ps -a --format "{{.ID}}\t{{.Names}}" | grep "stateful-auth-" | cut -f1').read().split('\n')
    containers.remove('')
    if len(containers) > 0:
        print("There are still {} containers created".format(containers))
        for container in containers:
            print("Stopping container {}".format(container))
            os.system("docker container stop {}".format(container))


if __name__ == "__main__":
    print("Pipeline started!")
    build_all_applications()
    while len(threads) > 0:
        pass
    remove_containers()
    threading.Thread(target=docker_compose_up).start()
