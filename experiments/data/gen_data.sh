#!/bin/bash

base=$1
reps=$2
gs=$3
ID=0

patterns=(
  'bridge'
  'chainOfResponsibility'
  'command'
  'composite'
  'decorator'
  'factory'
  'observer'
  'prototype'
  'proxy'
  'singleton'
  'state'
  'strategy'
  'template'
  'visitor'
)
grime=( 
  'pig'
  'peag'
  'peeg'
  'tig'
  'teag'
  'teeg'
  'dipg'
  'disg'
  'depg'
  'desg'
  'iipg'
  'iisg'
  'iepg'
  'iseg'
  'picg'
  'pirg'
  'pecg'
  'perg'
  'mpicg'
  'mpiug'
  'mpecg'
  'mpeug'
  'mticg'
  'mticg'
  'mtiug'
  'mtecg'
  'mteug'
)

mkdir $base
echo "ID,Pattern Type,Grime Type,Grime Severity,Directory" > "${base}/config.csv"

for (( j=1; j <= $reps; j++ )); do
  for p in ${patterns[@]}; do
    mkdir -p "${base}/rep_${j}/${p}/inst_${g}_${i}/"
    cp -ar "control/${p}/." "${base}/rep_${j}/${p}/control/"
    echo "${ID},${p},${g},${i},${base}/rep_${j}/${p}/control/" >> "${base}/config.csv"
    ((ID=ID+1))
    for g in ${grime[@]}; do
      for (( i=1; i <= $gs; i++ )); do
        mkdir -p "${base}/rep_${j}/${p}/inst_${g}_${i}/"
        cp -ar "control/${p}/." "${base}/rep_${j}/${p}/inst_${g}_${i}/"
        echo "${ID},${p},${g},${i},${base}/rep_${j}/${p}/inst_${g}_${i}/" >> "${base}/config.csv"
        ((ID=ID+1))
      done
    done
  done
done
