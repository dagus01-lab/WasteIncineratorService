basicrobot() {
  echo "Starting basicrobot..."
  cd /basicrobot24-1.0/bin
  ./basicrobot24
}

# Start the second process in the background
wis() {
  echo "Starting wis..."
  sleep 10
  cd /waste_incinerator_service-1.0/bin
  ./waste_incinerator_service  
}

# Run both functions in parallel
basicrobot &   
PID1=$!         

wis &   
PID2=$!         

wait $PID1 $PID2