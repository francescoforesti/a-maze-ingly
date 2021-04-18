# a-Maze-ingly retro puzzle

This is a Spring Boot application with a single http endpoint.
The Swagger/OpenAPI generated page can be found at 

`http://localhost:9090/api/swagger-ui/index.html`

List of commands to setup and run the project in a Docker container:

```
docker build -t mytest .
docker run -v $(pwd):/mnt -p 9090:9090 -w /mnt mytest ./scripts/build.sh 
docker run -v $(pwd):/mnt -p 9090:9090 -w /mnt mytest ./scripts/tests.sh 
docker run -v $(pwd):/mnt -p 9090:9090 -w /mnt mytest ./scripts/run.sh
```

Please note that the `/mnt/lib` folder serves as a temporary `~/.m2/repository` for Maven, 
so that subsequent builds do not need to download the dependencies all over again.
