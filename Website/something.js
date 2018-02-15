//$ npm install pug

//- template.pug
p #{name}'s Pug source code!

const pug = require('pug');

// Compile the source code
const compiledFunction = pug.compileFile('template.pug');

// Render a set of data
console.log(compiledFunction({
  name: 'Timothy'
}));

// Render another set of data
console.log(compiledFunction({
  name: 'Forbes'
}));

const pug = require('pug');

console.log(pug.renderFile('template.pug', {
  name: 'Timothy'
}));
