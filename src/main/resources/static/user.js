const getUsersPath = "/rest/user";
const getRolesPath = "/rest/role";
const updateUserPath = "/rest/user/";
const deleteUserPath = "/rest/user/";
const createUserPath = "/rest/user/";

populateAll();

async function get(url) {
    let response = await fetch(url);
    return await response.json();
}

async function populateAll() {
    let tableContent =
        `<thead style="border-bottom-width: 2px;">
        <tr>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Roles</th>
            <th>Edit</th>
            <th>Delete</th>
        </tr>
        </thead>
        <tbody>`;

    let data = await get(getUsersPath);

    for (let user of data) {
        tableContent +=
            `<tr>
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.email}</td>
                <td>${user.roles.map(r => r.name).join(' ')}</td>
                <td>
                    <button type="button"
                            class="btn btn-sm btn-success"
                            data-bs-toggle="modal"
                            data-bs-target="${'#editModal' + user.id}">Edit</button>
                    <div class="modal fade" id="${'editModal' + user.id}" tabindex="-1">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="${'editModalLabel' + user.id}">Edit User</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body mx-auto text-center">
                                    <div class="mb-3">
                                        <label class="form-label fw-bold" for="${'edit-id' + user.id}">ID</label>
                                        <input type="text" class="form-control" id="${'edit-id' + user.id}" name="id" value="${user.id}" disabled>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold" for="${'edit-firstName' + user.id}">First Name</label>
                                        <input type="text" class="form-control" id="${'edit-firstName' + user.id}" name="firstName" value="${user.firstName}">
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold" for="${'edit-lastName' + user.id}">Last Name</label>
                                        <input type="text" class="form-control" id="${'edit-lastName' + user.id}" name="lastName" value="${user.lastName}">
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold" for="${'edit-email' + user.id}">Email</label>
                                        <input type="text" class="form-control" id="${'edit-email' + user.id}" name="email" value="${user.email}">
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold" for="${'edit-password' + user.id}">Password</label>
                                        <input type="password" class="form-control" id="${'edit-password' + user.id}" name="password">
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold" for="${'edit-roles' + user.id}">Roles</label>
                                        <select class="form-select" id="${'edit-roles' + user.id}" name="roles" multiple>
                                            ${(await get(getRolesPath)).map(r => '<option value="' + r.name + '">' + r.name + '</option>')}
                                        </select>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary" data-bs-dismiss="modal" onclick="editUser(${user.id})">Save</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>
                <td>
                    <button type="button"
                            class="btn btn-sm btn-danger"
                            data-bs-toggle="modal"
                            data-bs-target="${'#deleteModal' + user.id}">Delete</button>
                    <div class="modal fade" id="${'deleteModal' + user.id}" tabindex="-1">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="${'deleteModalLabel' + user.id}">Delete User</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                </div>
                                <div class="modal-body mx-auto text-center">
                                    <div class="mb-3">
                                        <label class="form-label fw-bold" for="${'delete-id' + user.id}">ID</label>
                                        <input type="text" class="form-control" id="${'delete-id' + user.id}" name="id" value="${user.id}" disabled>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold" for="${'delete-firstName' + user.id}">First Name</label>
                                        <input type="text" class="form-control" id="${'delete-firstName' + user.id}" name="firstName" value="${user.firstName}" disabled>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold" for="${'delete-lastName' + user.id}">Last Name</label>
                                        <input type="text" class="form-control" id="${'delete-lastName' + user.id}" name="lastName" value="${user.lastName}" disabled>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold" for="${'delete-email' + user.id}">Email</label>
                                        <input type="text" class="form-control" id="${'delete-email' + user.id}" name="email" value="${user.email}" disabled>
                                    </div>
                                    <div class="mb-3">
                                        <label class="form-label fw-bold" for="${'delete-roles' + user.id}">Roles</label>
                                        <select class="form-select" id="${'delete-roles' + user.id}" name="roles" multiple disabled>
                                            ${user.roles.map(r => '<option value="' + r.name + '" >' + r.name + '</option>')}
                                        </select>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-danger" data-bs-dismiss="modal" onclick="deleteUser(${user.id})">Delete</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>`;
    }
    tableContent += '</tbody>';

    document.getElementById('adminAllUsers').innerHTML = tableContent;
}

async function editUser(id) {
    let user = {
        id: document.getElementById('edit-id' + id).value,
        firstName: document.getElementById('edit-firstName' + id).value,
        lastName: document.getElementById('edit-lastName' + id).value,
        email: document.getElementById('edit-email' + id).value,
        password: document.getElementById('edit-password' + id).value,
        roles: [...document.getElementById('edit-roles' + id).selectedOptions].map(o => o.value)
    };

    await fetch(updateUserPath + id, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user)
    });

    await populateAll();
}

async function deleteUser(id) {
    await fetch(deleteUserPath + id, { method: 'DELETE' });

    await populateAll();
}

async function createUser() {
    let user = {
        firstName: document.getElementById('new-firstName').value,
        lastName: document.getElementById('new-lastName').value,
        email: document.getElementById('new-email').value,
        password: document.getElementById('new-password').value,
        roles: [...document.getElementById('new-roles').selectedOptions].map(o => o.value)
    };

    await fetch(createUserPath, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(user)
    });

    await populateAll();
}