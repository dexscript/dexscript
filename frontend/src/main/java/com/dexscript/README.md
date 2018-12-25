# packages

AST can be viewed as a in memory database. 
Frontend build the database and make it queryable.

* ast: the bottom layer, parse abstract syntax tree from source
* type: based on ast, provide TypeSystem
* infer: attach type property to ast element
* analyze: check syntax and semantic error
