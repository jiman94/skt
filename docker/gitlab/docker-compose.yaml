# docker-compose.yml
web:
    image: 'gitlab/gitlab-ce:latest'
    restart: always
    hostname: 'gitlab.pilot.com'
    container_name: gitlab
    environment:
      GITLAB_OMNIBUS_CONFIG: |
        external_url 'http://gitlab.pilot.com'
    ports:
      - '80:80'
      - '443:443'
      - '22:22'
    volumes:
      - './srv/gitlab/config:/etc/gitlab'
      - './srv/gitlab/logs:/var/log/gitlab'
      - './srv/gitlab/data:/var/opt/gitlab'
      - './srv/gitlab/backups:/var/opt/gitlab/backups'