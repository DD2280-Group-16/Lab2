#!/usr/bin/env bash
# Script that pulls the latest of the current branch nad runs all the tests
set -e
BRANCH="$1"

if [ -z "$BRANCH" ]; then
    echo "No branch given"
    exit 1
fi

PROJECT_ROOT=$(git rev-parse --show-toplevel)
cd "$PROJECT_ROOT"

echo "Changing to $BRANCH"
git checkout "$BRANCH"

echo "Pulling latest"
git pull origin "$BRANCH"

echo "Running tests"
./mvnw test