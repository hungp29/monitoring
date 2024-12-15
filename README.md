#### Repository Structure
- `alert_manager`: Contains the configuration for `alertmanager`. The `docker-compose` file uses the `alertmanager.yml` file in this directory to initialize the alertmanager instance.
- `grafana`: Contains the configuration to initialize a Grafana instance.
	- `dashboards`: Stores the dashboard JSON files.
	- `provisioning`: Contains configuration files for initializing `datasources` and `dashboards`.
- `prometheus`: Contains configuration files to initialize the `Prometheus` instance, along with `alert` and `record rule` files.
- `demo-monitoring`: A demo application built using `Scala` and the `play-framework`.
- `test-plan`: Contains scripts for `JMeter` to simulate API calls.

#### Usage Guide
##### Configuration
- Alertmanager: 
  - Update the values for `smtp_from`, `smtp_auth_username`, `smtp_auth_password`, and `to` in the file `./alert_manager/alertmanager.yml`.
  - `smtp_auth_password`: Generate an app password in your Google account.
- Grafana: 
  - If you have a new dashboard, export it as a JSON file and save it in the folder `./grafana/dashboards`.
  - To add a new datasource during the initialization of the Grafana instance, you can either update the file `./grafana/provisioning/datasources/prometheus-datasource.yml` or create a new YAML file in the folder `./grafana/provisioning/datasources`.
- Prometheus:
  - Add new alerts to the file `./prometheus/alerts/application_alerts.yml` or create a new YAML file in the folder `./prometheus/alerts`.
  - Add new record rules to the file `./prometheus/record_rules/record_rule.yml` or create a new YAML file in the folder `./prometheus/record_rules`.
  - You can modify the Prometheus configuration in the file `./prometheus/prometheus.yml`.
- Test Plan with JMeter:
  - The current setup uses the Docker image `justb4/jmeter` to run the JMX test script.
  - Current script:
    - Continuously calls the `hello` and `random` APIs for 6 minutes. You can change this duration in the file `./test-plan/call_api_random.jmx`, under `ThreadGroup.duration` (two locations corresponding to the two APIs).
- Demo application: 
  - Ensure the application runs on port `9000`. If the port is changed, you need to update this value in the relevant configuration files.
#### Execution
- Start the application on the default port `9000`.
- `Prometheus`, `Alertmanager`, `Grafana` using `Docker Compose`:
  - Start: `docker-compose up -d`
  - Stop: `docker-compose down`
- Test plan
  - Run the following command: `docker run --rm -v ./test-plan/call_api_random.jmx:/jmeter/test_plan.jmx justb4/jmeter -n -t /jmeter/test_plan.jmx`