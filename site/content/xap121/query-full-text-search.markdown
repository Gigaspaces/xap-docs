---
type: post121
title:  Full Text Search
categories: XAP121
parent: querying-the-space.html
weight: 360
---

{{%warning%}}
This page is under construction.
{{%/warning%}}


XAP 12.1 introduces full text search leveraging the {{%exurl "Lucene full-text" "http://lucene.apache.org"%}} search engine library.

- Keyword matching
- Search for phrase 
- Wildcard matching
- Proximity matching  
- Range searching
- Boosting a term.
- Regular expression queries
- Fuzzy search
    

 

# Defining ....

# Query 


# Query multiple fields



# Indexing




# Space Document



# Configuration


|   Property   | Description |  Default |
|--------------|-------------|----------|
|lucene.storage.location        | The location of the lucene index|Deploy path of this space instance, when deployed in the service grid. When not deployed in the service grid <user.dir>/xap/full_text_search|
|lucene.storage.directory-type  | The directory type. Available values: MMapDirectory, RAMDirectory. | MMapDirectory|
|<nobr>lucene.max-uncommitted-changes<nobr> | The buffer size of uncommitted changes. When user write indexed document to the space, the document doesn’t flushes to the lucene index immediately. It flushes after search or after overflowing the buffer.| ? |


 