# AndrewDolby.com [![Build Status](https://travis-ci.org/adolby/AndrewDolby.com.svg?branch=master)](https://travis-ci.org/adolby/AndrewDolby.com)

My profile website.

The following features are powered by ClojureScript:

1. Download and analyze GitHub Release info to display download links for my projects. This is accomplished with core.async channels, which are the Clojure(Script) version of Go channels. Channels are also known as co-routines or communicating sequential processes in other languages.

2. Template from valid HTML markup with kioo and Reagent, a ClojureScript React wrapper.

3. Swap the site theme around with the footer links
