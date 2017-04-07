#!/bin/bash
sum=0
i=0
while(( i <= 10000 ))
do
   adb shell input tap 350 1100
   date +%F" "%H:%M:%S
   sleep 1.5
done
