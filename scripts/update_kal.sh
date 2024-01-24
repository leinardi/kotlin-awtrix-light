#!/bin/bash

#
# Copyright 2024 Roberto Leinardi.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

kal_dir="/opt/kal"


# Step 1: Get the version of the jar "kal.jar"
script_dir="$(dirname "$(readlink -f "$0")")"
jar_version=$(java -jar ${kal_dir}/kal.jar -v)

# Check the exit code after getting the version
if [ $? -ne 0 ]; then
    echo "Failed to get the version of kal.jar."
    exit 1
fi

echo "Current KAL version: $jar_version"

# Step 2: Run the script "fetch_release.py" with the jar version as a parameter
"$script_dir/fetch_release.py" "$kal_dir" "$jar_version"
