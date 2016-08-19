# AndrewDolby.com [![Build Status](https://travis-ci.org/adolby/AndrewDolby.com.svg?branch=master)](https://travis-ci.org/adolby/AndrewDolby.com)

My profile website. https://www.andrewdolby.com

## Features

1. Downloads and analyzes GitHub Release info to display download links for my projects that exist as executables.

2. Swap the site theme around with the footer links

## Implementation Details

1. Uses re-frame and Reagent. re-frame is a Flux implementation that supplies a reactive flow template for structuring a ClojureScript app. Reagent is a ClojureScript React wrapper (and more!) that provides great performance while taking care of view updates as you change your application state.

2. Template from valid HTML markup with kioo

## License
MIT
