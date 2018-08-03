#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo "Parser"
${DIR}/lib/yacc.macosx -J -Jstack=10000 -Jpackage=daikon.parser ${DIR}/calc.y
mv ${DIR}/*.java ${DIR}/../src/main/java/daikon/parser/
echo "Done"
