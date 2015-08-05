## **Overview**
## **Key Functionalities**



> 1. **Ingest mailbox**
> 2. **Make Neo4j Graph Database**
> 3. **Auto Indexing**
> 4. **Topic modeling and Sentiment Extraction**
> 5. **Pre-defined Query Support**

## **System Architecture**

![process flow](/flowchart.png)
![Neo4j DB architecture](/Arch2.png)

## **Getting Started**
### **Software Requirements**
    JDK (Version 7 or above)
    Neo4j (version 2.2.X)
    Thrift server (Apache Thrift 0.9.2)
    Python (version 2.7.X)
    Pip (version 7.1.X)

##### **_Workspace to Download_**
  > [E-analysis](https://gitlab.com/nishantgandhi99/EmailNeo4j.git)

  > [DisKoveror-ta](https://github.com/serendio-labs/diskoveror-ta/archive/master.zip) 
##### **_Starting Thrift servers for Sentiment and Topics in DisKoveror-ta_**

The requirements.txt file specifies the software packages along with their versions to be installed. Execute the
below command to install all python related dependencies for the Sentiment and Topics.

>     /diskoveror-ta/src/main/python$ sudo pip install -r requirements.txt

Start the thrift servers for Topics and Sentiments 

>     /diskoveror-ta/src/main/python$ python server.py

##### **_Configuring Build Path_**

Add all the given libraries in **_"lib"_** folder to your build path

##### **_Using Sample Data_**

Sample email data files in **_"data"_** folder have been provided for your convenience and can be used to test the project:
>     .pst
>     .mbox
>     .eml

