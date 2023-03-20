#!/bin/bash

set -o errexit -o nounset

boot -u
boot -V

boot prod

rm -rf .git
cd target
rm -rf app.cljs.edn

git init
git config user.name "GitHub Actions"
git config user.email "andrewdolby@gmail.com"
git config --global push.default simple
git remote add upstream "https://${PAGES_TOKEN}@github.com/adolby/adolby.github.io.git"

echo "andrewdolby.com" > CNAME
touch .

git add -A .
git commit -m "Deploy to GitHub Pages"
git push --force upstream master
