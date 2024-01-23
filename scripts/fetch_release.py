#!/usr/bin/env python3

#  Copyright 2024 Roberto Leinardi.
#
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.

from typing import List, Dict, Union, Optional
import os
import requests
import sys

_REPO_OWNER = "leinardi"
_REPO_NAME = "kotlin-awtrix-light"


def get_latest_release(repo_owner: str, repo_name: str) -> Optional[tuple[str, List[Dict[str, Union[str, List[str]]]]]]:
    api_url = f"https://api.github.com/repos/{repo_owner}/{repo_name}/releases"
    response = requests.get(api_url)

    if response.status_code == 200:
        releases_data = response.json()
        if releases_data:
            latest_release = releases_data[0]
            tag_name: str = latest_release["tag_name"]
            assets: List[Dict[str, Union[str, List[str]]]] = latest_release["assets"]
            return tag_name, assets
    return None


def download_release_assets(
    assets: List[Dict[str, Union[str, List[str]]]],
    destination_dir: str,
    release_tag: str
) -> None:
    for asset in assets:
        asset_name: str = asset["name"]
        file_name: str = asset_name.replace(f"-fat-{release_tag}", "")
        asset_url: str = asset["browser_download_url"]
        download_path: str = os.path.join(destination_dir, file_name)

        with open(download_path, 'wb') as f:
            f.write(requests.get(asset_url).content)

        print(f"Downloaded: {asset_name} to {download_path}")


if __name__ == "__main__":
    latest_release_tag, release_assets = get_latest_release(_REPO_OWNER, _REPO_NAME)

    target_release_tag: Optional[str] = None

    download_dir = sys.argv[1]

    # Check if the target_release_tag is provided as a command-line argument
    if len(sys.argv) > 2:
        target_release_tag = sys.argv[2]

    if target_release_tag is not None and target_release_tag == latest_release_tag:
        print(f"Nothing to download: release tag {latest_release_tag} is the latest release.")
        exit(1)
    elif latest_release_tag:
        print(f"Latest Release Tag: {latest_release_tag}")
        download_release_assets(release_assets, download_dir, latest_release_tag)
        exit(0)
    else:
        print("Unable to fetch the latest release.")
        exit(2)
