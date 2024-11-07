#!/bin/ash

# Function to handle SIGTERM gracefully
term_signal_handler() {
  echo "############  Caught SIGTERM #############"
  docker stop devices_volume_priv >/dev/null 2>&1
  exit
}

# Set up the signal trap
trap 'term_signal_handler' SIGTERM

# Starting the event listener container
echo "Starting event listener container"
docker run \
  --rm \
  --privileged \
  --tty=false -i \
  --name devices_volume_priv \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v /sys/fs/cgroup/devices/docker/:/docker/:Z \
  -v /dev/:/real/dev/ \
  docker sh -s <<"EOF" &
    # Function to add device permissions
    add_perm() {
      read DEVICE CID
      if [[ -z $$DEVICE ]]; then
        echo "Startup detected"
        return
      fi

      USBDEV=`readlink -f /real$${DEVICE}`
      major=`stat -c '%t' $$USBDEV`
      minor=`stat -c '%T' $$USBDEV`
      if [[ -z $$minor || -z $$major ]]; then
        echo 'Device not found'
        return
      fi
      dminor=$$((0x$${minor}))
      dmajor=$$((0x$${major}))
      echo "Setting permissions (c $$dmajor:$$dminor rwm) for $${CID} to device ($${DEVICE})"
      echo "c $$dmajor:$$dminor rwm" > /docker/$${CID}/devices.allow
    }

    # Listen for Docker events with the 'volume.device=gpio' label
    echo "Listening for startup events with label 'volume.device=gpio'"
    while true; do
      docker events \
        --filter 'label=volume.device=gpio' \
        --filter 'event=start' \
        --format '{{index .Actor.Attributes "volume.device"}} {{.Actor.ID}}' | \
        add_perm
      echo "Restarting events listener"
    done
EOF

# Keep the script running indefinitely to prevent the container from exiting
while true ; do 
  sleep 5
done