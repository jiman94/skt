# Build

Pre-requisites
--------------
**1.Clone project**
```
git clone  http://bitbucket.chicor.co.kr:7990/scm/chic/apm-scouter.git
cd scouter-docker
export SCOUTER_HOME=$PWD
```
**2.Update Scouter Version**
```
vi config
SCOUTER_VERSION=2.8.1
```
**3.Download Scouter Release**
By default, preq.sh do not need direct url but specifying the direct url is acceptable.
curl -O -L  https://github.com/scouter-project/scouter/releases/download/v2.8.1/scouter-all-2.8.1.tar.gz
```
sh /preq.sh 
```

Scouter Server
---------------
**1. Execute build-docker.sh**
This shell create a new Scouter Server Image with tag "v${SCOUTER_VERSION} and latest"

```
cd ${SCOUTER_HOME}/scouter-server
./build-docker.sh
```

```
cd ${SCOUTER_HOME}/scouter-host-agent
./build-docker.sh
```



**2.Test**
```

docker run -it --name scouter-server -p 6100:6100/tcp -p 6101:6101/udp chicor/scouter-server


 / ___|  ___ ___  _   _| |_ ___ _ __ 
 \___ \ / __/   \| | | | __/ _ \ '__|
  ___) | (_| (+) | |_| | ||  __/ |   
 |____/ \___\___/ \__,_|\__\___|_|                                      
 
```

**2.Test**
```
docker run -it --link scouter-server chicor/scouter-host-agent

  ____                  _            
 / ___|  ___ ___  _   _| |_ ___ _ __ 
 \___ \ / __/   \| | | | __/ _ \ '__|
  ___) | (_| (+) | |_| | ||  __/ |   
 |____/ \___\___/ \__,_|\__\___|_|                                      
 
Configure -Dscouter.config=./conf/scouter.conf

```

Scouter Test App
----------------
**1. Execute build-docker.sh**
```
cd ${SCOUTER_HOME}/scouter-test-app
./build-docker.sh
```


