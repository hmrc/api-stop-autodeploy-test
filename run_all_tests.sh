#!/usr/bin/env bash
sbt clean compile coverage scalafmtAll scalafixAll test coverageOff coverageReport
