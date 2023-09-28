import datetime
import random
import subprocess
import sys
import time

def parse_interval(interval_str):
    if interval_str.endswith('s'):
        return int(interval_str[:-1])
    elif interval_str.endswith('m'):
        return int(interval_str[:-1]) * 60
    else:
        return int(interval_str)

def get_pods(deployment):
    output = run_kubectl(
        ["get", "pods", "-l", f"app.kubernetes.io/name={deployment}", "-o", "jsonpath='{.items[*].metadata.name}'"])
    return output[1:-1].split(" ")


def kill_pod(random_pod):
    output = run_kubectl(["delete", "pod", "--force=true", random_pod])
    print(output)


def kill_pods(interval, deployments):
    while True:
        print("-" * 50)
        print(datetime.datetime.now())

        pods = []
        for deployment in deployments:
            pods += get_pods(deployment)
        print("Available pods:")
        print(*pods, sep="\n")
        random_pod = random.choice(pods)
        print("Killing pod: ", random_pod)
        kill_pod(random_pod)
        print("-" * 50)
        time.sleep(interval)


def run_kubectl(params):
    proc = subprocess.Popen(["kubectl", *params], stdin=subprocess.PIPE, stdout=subprocess.PIPE)
    result = proc.communicate()
    if proc.returncode != 0:
        print("Error running command")
        exit(1)
    return result[0].decode('utf-8')


def print_cluster_info():
    print("Running on cluster:")
    output = run_kubectl(["cluster-info"])
    print(output)


def main():
    if len(sys.argv) < 3:
        print("Usage python aapps-monkey {interval} {deployment} [{deployment}, {deployment}]")
        print("interval is the time between pod deletion in minutes")
        print("deployment is the name of the deployment to get the pods from")
        print("this script assumes that kubectl is in the path and configured to reach the cluster.")
        exit(1)

    interval = parse_interval(sys.argv[1])
    deployments_str = sys.argv[2]
    # Split the comma-separated deployments string into a list
    deployments = deployments_str.split(',')
    
    print_cluster_info()
    print("interval:", interval)
    print("deployments:", deployments)
    confirmation = input("Continue? (Y/N)")
    while confirmation not in ("Y", "N"):
        confirmation = input("Continue? (Y/N)")

    if confirmation == "Y":
        kill_pods(interval, deployments)


if __name__ == '__main__':
    main()
