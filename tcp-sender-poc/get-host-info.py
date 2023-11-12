import subprocess
import json

# Get the nodePort using kubectl
node_port_command = "kubectl get --namespace default -o jsonpath='{.spec.ports[0].nodePort}' services demo-backend"
node_port_process = subprocess.Popen(node_port_command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
node_port_output, node_port_error = node_port_process.communicate()

# Get the node IP using kubectl
node_ip_command = "kubectl get nodes --namespace default -o jsonpath='{.items[0].status.addresses[0].address}'"
node_ip_process = subprocess.Popen(node_ip_command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
node_ip_output, node_ip_error = node_ip_process.communicate()

# Check if there were any errors
if node_port_process.returncode != 0:
    print(f"Error getting nodePort: {node_port_error.decode('utf-8')}")
    exit(1)

if node_ip_process.returncode != 0:
    print(f"Error getting node IP: {node_ip_error.decode('utf-8')}")
    exit(1)

# Extract values from the command output
node_port = node_port_output.decode('utf-8')
node_ip = node_ip_output.decode('utf-8')

# Construct the URL
url = f"http://{node_ip.strip()}:{node_port.strip()}"
print(url)
