import RPi.GPIO as gpio
import time
from collections import deque
DT =23 #pin 16
SCK=24 #pin 18
calibration=105 #calculate proper calibration as: (raw_value-tare_value)/known_weight
curSample=0
window = deque()
gpio.setwarnings(False)
gpio.setmode(gpio.BCM)
gpio.setup(SCK, gpio.OUT)
gpio.setup(DT, gpio.IN)
def readCount():
  i=0
  Count=0
  while gpio.input(DT) != 0:
    pass

  for _ in range(24):
    gpio.output(SCK,True)
    gpio.output(SCK,False)
    Count <<= 1
    Count |= gpio.input(DT)
    #time.sleep(0.001)
  #print(Count)
  Count=Count ^ 0x800000
  return Count  

def readAverage(num_samples=5):
  total = 0
  for _ in range(num_samples):
    total += readCount()
    time.sleep(0.01)  
  return (total / num_samples)

def movingAverage(new_value, window, window_size=20):
    window.append(new_value)
    if len(window) > window_size:
        window.popleft()
    return sum(window) / len(window)

time.sleep(3)
tare= readCount()
#print("tare successfully calculated!")
while 1:
  curSample= readCount() #readAverage()
#  curSampleSmoothed= movingAverage(curSample, window)
  w=abs(int((curSample-tare)/calibration)) 
  print(w, flush=True ) #measured in grams
  time.sleep(0.25)
