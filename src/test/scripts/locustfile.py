from locust import HttpLocust, TaskSet, task

class WebsiteTasks(TaskSet):

    # 500 Max
    # @task(500)
    @task
    def action_landing_page(self):
        self.client.get("/messages", auth=("user", "password"))

class WebsiteUser(HttpLocust):
    # host="http://localhost"
    task_set = WebsiteTasks
    min_wait = 0
    max_wait = 0
#    min_wait = 5000
#    max_wait = 15000