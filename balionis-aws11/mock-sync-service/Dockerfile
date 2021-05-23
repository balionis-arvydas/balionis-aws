FROM rodolpheche/wiremock:2.27.2

COPY src/main/resources/mappings/*.json /home/wiremock/mappings/

COPY src/main/resources/responses/ /home/wiremock/__files/responses/

CMD ["--verbose", "--global-response-templating"]