Installing windows service:

prunsrv //IS//TowerServer --DisplayName="TowerServer" --Install="C:\Users\user_local\Desktop\pt-ar-game-4\TowerProxy\run\prunsrv.exe" --Jvm=auto --StartMode=jvm --StopMode=jvm --StartClass=com.playtech.ptargame4.server.Starter --StartMethod start --StopClass=com.playtech.ptargame4.server.Starter  --StopMethod stop --Startup=auto --Classpath="C:\Users\user_local\Desktop\pt-ar-game-4\TowerProxy\server\server-impl\target\server-impl-1.0-SNAPSHOT-jar-with-dependencies.jar" --JvmOptions=-Djava.util.logging.config.file=conf/logging.properties ++JvmOptions=-XX:+UseG1GC

Uninstalling windows service:
prunsrv //DS//TowerServer
