import at.phatbl.shellexec.ShellExec

tasks {
    val dockerImage = extra["dockerImage"]

    create<ShellExec>("dockerBuild") {
        description = "Build a service docker image"
        group = "Docker"
        command = "docker build -t $dockerImage:latest ."
    }

    create<ShellExec>("dockerPush") {
        dependsOn("dockerBuild")

        description = "Push a service docker image to kubernetes"
        group = "Docker"
        command = "kind load docker-image --name wsl2 $dockerImage:latest"
    }

    create<ShellExec>("dockerComposeUp") {
        dependsOn("dockerBuild")

        description = "Start a service on local docker"
        group = "Docker"
        command = "docker-compose -f docker-compose.yml build && docker-compose -f docker-compose.yml up -d"
    }

    create<ShellExec>("dockerComposeDown") {
        description = "Stop a service on local docker"
        group = "Docker"
        command = "docker-compose -f docker-compose.yml down"
    }
}
