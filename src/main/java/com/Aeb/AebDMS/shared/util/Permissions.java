package com.Aeb.AebDMS.shared.util;

import java.util.List;
import java.util.Map;

public class Permissions {

    // User Management
    public static final String USER_READ = "user:read";
    public static final String USER_UPDATE = "user:update";
    public static final String USER_DELETE = "user:delete";
    public static final String USER_ASSIGN_ROLE = "user:assign-role";
    public static final String USER_CHANGE_PASSWORD = "user:change-password";
    public static final String USER_DISABLE = "user:disable";
    public static final String USER_RESET_PASSWORD = "user:reset-password";

    // Role & Permission Management
    public static final String ROLE_READ = "role:read";
    public static final String ROLE_UPDATE = "role:update";
    public static final String ROLE_DELETE = "role:delete";
    public static final String ROLE_ASSIGN_PERMISSION = "role:assign-permission";

    // Group Management
    public static final String GROUP_READ = "group:read";
    public static final String GROUP_UPDATE = "group:update";
    public static final String GROUP_DELETE = "group:delete";
    public static final String GROUP_ASSIGN_USER = "group:assign-user";
    public static final String GROUP_ASSIGN_ROLE = "group:assign-role";

    // Folder Management
    public static final String FOLDER_READ = "folder:read";
    public static final String FOLDER_UPDATE = "folder:update";
    public static final String FOLDER_CREATE = "folder:write";
    public static final String FOLDER_DELETE = "folder:delete";
    public static final String FOLDER_SHARE = "folder:share";
    public static final String FOLDER_MOVE = "folder:move";
    public static final String FOLDER_CHANGE_PERMISSIONS = "folder:change-permissions";

    // Document Management
    public static final String DOCUMENT_READ = "document:read";
    public static final String DOCUMENT_WRITE = "document:write";
    public static final String DOCUMENT_UPDATE = "document:update";
    public static final String DOCUMENT_DELETE = "document:delete";
    public static final String DOCUMENT_EDIT = "document:edit";
    public static final String DOCUMENT_SHARE = "document:share";
    public static final String DOCUMENT_LOCK = "document:lock";
    public static final String DOCUMENT_UNLOCK = "document:unlock";
    public static final String DOCUMENT_VERSION_CONTROL = "document:version-control";
    public static final String DOCUMENT_SIGN = "document:sign";
    public static final String DOCUMENT_ASSIGN_REVIEWER = "document:assign-reviewer";
    public static final String DOCUMENT_VALIDATE = "document:validate";
    public static final String DOCUMENT_ANNOTATE = "document:annotate";
    public static final String DOCUMENT_DOWNLOAD = "document:download";

    // OCR & Search
    public static final String OCR_READ = "ocr:read";
    public static final String OCR_REPROCESS = "ocr:reprocess";
    public static final String SEARCH_ADVANCED = "search:advanced";
    public static final String SEARCH_FULL_TEXT = "search:full-text";

    // Workflow & Validation
    public static final String WORKFLOW_VIEW_HISTORY = "workflow:view-history";
    public static final String WORKFLOW_ASSIGN = "workflow:assign";
    public static final String WORKFLOW_APPROVE = "workflow:approve";
    public static final String WORKFLOW_REJECT = "workflow:reject";
    public static final String WORKFLOW_CANCEL = "workflow:cancel";

    // Dashboard & Analytics
    public static final String DASHBOARD_VIEW = "dashboard:view";
    public static final String DASHBOARD_EDIT_WIDGETS = "dashboard:edit-widgets";
    public static final String REPORT_GENERATE = "report:generate";
    public static final String REPORT_DOWNLOAD = "report:download";

    // UI Customization & System Settings
    public static final String UI_CUSTOMIZE_THEME = "ui:customize-theme";
    public static final String UI_MANAGE_WIDGETS = "ui:manage-widgets";

    // Server & Admin Panel
    public static final String SERVER_VIEW_STATUS = "server:view-status";
    public static final String SERVER_MANAGE_LOGS = "server:manage-logs";
    public static final String SERVER_UPDATE_CONFIG = "server:update-config";
    public static final String SERVER_BACKUP = "server:backup";
    public static final String SERVER_RESTORE = "server:restore";
    public static final String SERVER_MANAGE_STORAGE = "server:manage-storage";
    public static final String SERVER_RESTART = "server:restart";

    // Audit & Logs
    public static final String AUDIT_READ = "audit:read";
    public static final String AUDIT_EXPORT = "audit:export";
    public static final String AUDIT_FILTER_BY_USER = "audit:filter-by-user";

    // Notification Management
    public static final String NOTIFICATION_READ = "notification:read";
    public static final String NOTIFICATION_SEND = "notification:send";

    // Trash & Recovery
    public static final String TRASH_VIEW = "trash:view";
    public static final String TRASH_RESTORE = "trash:restore";
    public static final String TRASH_DELETE_PERMANENTLY = "trash:delete-permanently";

    // Model & Metadata
    public static final String MODEL_WRITE = "model:write";
    public static final String MODEL_READ = "model:read";
    public static final String MODEL_UPDATE = "model:update";
    public static final String MODEL_DELETE = "model:delete";

}

