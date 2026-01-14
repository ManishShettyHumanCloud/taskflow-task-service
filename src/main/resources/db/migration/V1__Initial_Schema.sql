CREATE TABLE tasks (
                       task_id UUID PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       task_type VARCHAR(50) NOT NULL,
                       parent_task_id UUID,
                       project_id UUID NOT NULL,
                       status_id UUID NOT NULL,
                       assignee_id UUID,
                       priority VARCHAR(50),
                       position INTEGER NOT NULL,
                       due_date DATE,
                       start_date DATE,
                       created_at TIMESTAMP,
                       updated_at TIMESTAMP
);
CREATE INDEX idx_task_project ON tasks(project_id);
CREATE INDEX idx_task_project_status ON tasks(project_id, status_id);