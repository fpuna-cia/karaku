#!/bin/sh

#cd base/utilities/git
chmod +x base/utilities/git/hooks/commit-msg
cp base/utilities/git/hooks/commit-msg .git/hooks/commit-msg
git config commit.template utilities/git/.gitmessage.txt
