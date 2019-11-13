package com.example.conveniotaller.Modelo.Baseconvenio

class FieldPath(var fieldPath: String)
class CollectionId(var collectionId: String)
class Field(var fieldFilter: FieldFilter)
class FieldFilter(var field: FieldPath,
                  var op: String,
                  var value: StringValue)
class Where(var where: Field, var from: MutableList<CollectionId> )
class StructuredQuery(var structuredQuery: Where)