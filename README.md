# AndrewDolby.com

My profile website.

It uses ClojureScript to perform the following functions:

1. Download and analyze GitHub Release info to displaying download links for my projects. This is accomplished with core.async channels, which are the ClojureScript version of Go channels. Channels are also known as co-routines in other languages.
2. Template from real, uncluttered HTML markup with kioo
3. Swap the site theme around with the footer links
4. The above three features are performance-enhanced with Reagent, a React wrapper. Reagent and React improve DOM manipulation performance incredibly. I very much encourage you to take a look if you're considering using React.
