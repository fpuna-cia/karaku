#!/bin/sh
cd utilities/git
chmod +x hooks/commit-msg
cp hooks/commit-msg ../../.git/hooks/commit-msg
git config commit.template utilities/git/.gitmessage.txt
