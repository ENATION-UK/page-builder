<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Builder</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>

    <script src="image2code.js?v=0.1"></script>
    <style>
        body {
            /*background-color: #f8f9fa;*/
        }

        .container-fluid {
            height: 100%;
        }

        .container-fluid .max-height{
            height: 100%;
        }

        .fixed-side {
            width: 400px;
            margin-left: 10px;
            border-right: 1px solid #e5e7eb;
        }


        .flex-grow-1 {
            margin-top: 30px;
            flex-grow: 1; /* Allow the right column to take up the remaining space */
        }

        .upload-area {
            height: 300px;
            border: 2px dashed #dee2e6;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
            color: #6c757d;
            font-size: 1rem;
        }

        .upload-area:hover {
            border-color: #adb5bd;
            cursor: pointer;
        }

        .code-type {
            margin-top: 10px;
        }

        .settings-icon {
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;

        }

        .header {
            margin-top: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-sizing: border-box;
        }

        .dlg-footer {
            display: flex;
            flex-shrink: 0;
            flex-wrap: wrap;
            align-items: center;
            justify-content: flex-end;
        }

        .preview-block {
            --tw-shadow: 0 10px 15px -3px rgb(0 0 0 / .1), 0 4px 6px -4px rgb(0 0 0 / .1);
            --tw-shadow-colored: 0 10px 15px -3px var(--tw-shadow-color), 0 4px 6px -4px var(--tw-shadow-color);
            box-shadow: var(--tw-ring-offset-shadow, 0 0 #0000), var(--tw-ring-shadow, 0 0 #0000), var(--tw-shadow);
            width: 90%;
            height: 90%;
            border: 2px solid #e5e7eb;
            display: none;
        }

        iframe {
            width: 100%;
            height: 100%;
        }

        #imagePreview {
            display: none;
            width: 100%;
            border: 1px solid #e5e7eb;
            margin: 5px;
        }

        .menu {
            display: none
        }

        .menu button {
            margin: 5px;
        }
        .output{
            display: none;
            overflow-x: hidden;
            white-space: nowrap;
            margin: 5px;
            background-color: black;
            color: green;
            font-family: 'Courier New', Courier, monospace;
            font-size: 12px;
        }
    </style>
</head>
<body>
<input type="file" id="fileInput" accept="image/*" style="display:none;">

<div class="container-fluid">

    <div class="row">
        <#include "nav.ftlh"/>
    </div>


    <div class="row max-height">
        <div class="col-auto fixed-side">

            <select class="form-select code-type">
            </select>


            <div class="output">
            </div>
            <div class="menu">
                <div class="d-flex ">
                    <button class="btn btn-dark flex-grow-1" id="downloadBtn">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                             class="bi bi-cloud-arrow-down-fill" viewBox="0 0 16 16">
                            <path d="M8 2a5.53 5.53 0 0 0-3.594 1.342c-.766.66-1.321 1.52-1.464 2.383C1.266 6.095 0 7.555 0 9.318 0 11.366 1.708 13 3.781 13h8.906C14.502 13 16 11.57 16 9.773c0-1.636-1.242-2.969-2.834-3.194C12.923 3.999 10.69 2 8 2m2.354 6.854-2 2a.5.5 0 0 1-.708 0l-2-2a.5.5 0 1 1 .708-.708L7.5 9.293V5.5a.5.5 0 0 1 1 0v3.793l1.146-1.147a.5.5 0 0 1 .708.708"/>
                        </svg>
                        Download
                    </button>
                    <button class="btn btn-danger flex-grow-1" id="resetBtn">
                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                             class="bi bi-arrow-counterclockwise" viewBox="0 0 16 16">
                            <path fill-rule="evenodd"
                                  d="M8 3a5 5 0 1 1-4.546 2.914.5.5 0 0 0-.908-.417A6 6 0 1 0 8 2z"/>
                            <path d="M8 4.466V.534a.25.25 0 0 0-.41-.192L5.23 2.308a.25.25 0 0 0 0 .384l2.36 1.966A.25.25 0 0 0 8 4.466"/>
                        </svg>
                        Reset
                    </button>
                </div>
            </div>
            <img id="imagePreview">

        </div>
        <div class="col flex-grow-1 ">
            <div class="upload-area mb-3">
                Drag & drop a screenshot here,<br>
                or paste from clipboard,<br>
                or click to upload
            </div>
            <div class="mb-3 preview-block">
                <iframe id="preview"></iframe>
            </div>

        </div>
    </div>
</div>


<!-- Modal -->
<div class="modal fade" tabindex="-1" id="settingModal" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <form>
                    <div class="mb-3">
                        <label for="openaiKey" class="form-label">OpenAI API key</label>
                        <input type="text" class="form-control" id="openaiKey">
                        <div class="form-text">Only stored in your browser. Never stored on server</br>
                            仅保存在客户端，不会在服务器端存储
                        </div>
                    </div>
                    <div class="dlg-footer">
                        <button type="button" id="settingSaveBtn" class="btn btn-primary" data-bs-dismiss="modal">Save
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>