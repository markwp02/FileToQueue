# FileToQueue
 Consumes a File event and adds a message to a queue


Install Docker for desktop
Run the following command to get and run activemq image

```shell
docker run -d --name activemq -p 61616:61616 -p 8161:8161 webcenter/activemq
```

Access the admin console
http://localhost:8161/admin/
admin/admin