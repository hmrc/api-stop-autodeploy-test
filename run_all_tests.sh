#!/usr/bin/env bash
sbt clean compile coverage scalafmtAll test coverageOff coverageReport
