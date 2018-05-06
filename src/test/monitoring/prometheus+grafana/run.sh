#!/bin/bash

docker run --name automated-grafana -d -p 18190:9090 -p 6666:6666 \
       -e "ENVIRONMENT=perftest" \
       -e "GF_SERVER_HTTP_PORT=6666" \
       -e "WAITING_TIME=20" \
       -v $(pwd)/prometheus:/prometheus-config \
       -v $(pwd)/grafana:/dashboards \
       -v $(pwd)/users:/users \
       -v $(pwd)/sources:/sources \
       serragnoli/automated-grafana-prometheus
