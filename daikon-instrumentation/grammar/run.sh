#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo ${DIR}
${DIR}/jflex.sh
${DIR}/jyacc.sh
