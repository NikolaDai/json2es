# json2es
This project is aiming to import the jason file to Elasticsearch. This project is one module of NIRAS project. 
NIRAS : With the optimization of production processes such as news planning, collection and release, the number of news content has exploded. In addition, the production of news products relies heavily on historical news materials and requires an accurate, fast and comprehensive search system to support them. Despite the emergence of a number of commercial products in the industry, problems such as high fees and difficult customization have limited the further optimization of search services. The goal of the project is to build an open source, free and practical news information retrieval and analysis system (NIRAS) based on open source systems, which can meet the retrieval needs of news product production and explore a new way for news information retrieval.

本项目的目标是以最快的速度将JASON文件导入Elasticsearch库中。此项目是NIRAS项目中的子项目之一。NIRAS: 随着新闻策划、采集及发布等生产流程的优化，新闻内容数量呈现爆炸式增长。此外，新闻产品的制作对历史新闻资料的依赖程度很高，需要有准确、快速、全面的检索系统做支持。尽管行业内出现了一批商业化产品，但是其收费高、定制难等问题限制了检索服务的进一步优化。项目目标是，基于开源系统构建一套开源、免费且实用新闻信息检索及分析系统（NIRAS），能够满足新闻产品制作的检索需求，为新闻信息检索探索一条新路。

# Basics
1. JDK 1.8
2. Spring Boot 2.1.1
3. Elasticsearch 6.5.3 (with HanLP plugin installed)
4. Intellij IDEA(recommended)

# Data Source
The dataSet01.txt in this project is a json format file which could be used as the input file to json2es. The data in the body field includes some
other fields' data.

# Syllabus
1. learn the java api of Elasticsearch, please refer to https://www.elastic.co;

# Directory Instruction 
|---APIDemo(ES API DEMO codes)

|---Json2esAction (perform the action of importation from json to ES)

# Pair Coding
Welcome any modification to the existing codes
