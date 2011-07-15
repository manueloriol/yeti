#!/bin/bash

for (( i = 0 ; i < 106 ; i++ )) do
   sh ./runTestsComplement.sh &
   sleep 3600
done	
