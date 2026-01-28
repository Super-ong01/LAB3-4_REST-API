#!/usr/bin/env bash
set -euo pipefail

# Run Maven tests for the library project and print a colored summary
cd "$(dirname "$0")/.." || exit 1

echo "Running unit tests (mvn test)..."
mvn test
RC=$?
if [ "$RC" -eq 0 ]; then
  # green output
  printf "\e[32mALL TESTS PASSED\e[0m\n"
else
  printf "\e[31mSOME TESTS FAILED (exit %d)\e[0m\n" "$RC"
fi
exit $RC
