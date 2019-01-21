#!/bin/bash
export OUTPUT="$(sh ./release.sh)"
echo $OUTPUT
touch /release-$OUTPUT