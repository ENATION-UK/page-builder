<html>
<head>
    <#import "/spring.ftl" as spring/>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Builder</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="style.css?v=1.2">

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.min.js"></script>

    <script src="PageBuilder.js?v=0.2"></script>

</head>
<body>
<input type="file" id="fileInput" accept="image/*" style="display:none;">

<div class="container-fluid">

    <div class="row">
        <#include "nav.ftlh"/>
    </div>

    <div class="row max-height">
        <div id="chat-content" class="col-auto fixed-side  scroll-y" style="max-height: 900px">

            <select class="form-select code-type">
            </select>

            <div class="form-container form-req">
                <form>
                    <div class="mb-3">
                        <label for="industry" class="form-label"><@spring.message "form.industry" /></label>
                        <input type="text" class="form-control" id="industry" name="industry"
                               placeholder="<@spring.message "form.placeholder.industry" />">
                    </div>
                    <div class="mb-3">
                        <label for="pageType" class="form-label"><@spring.message "form.pageType" /></label>
                        <input type="text" class="form-control" id="pageType" name="pageType"
                               placeholder="<@spring.message "form.placeholder.pageType" />">
                    </div>
                    <div class="mb-3">
                        <label for="business" class="form-label"><@spring.message "form.business" /></label>
                        <input type="text" class="form-control" id="business" name="business"
                               placeholder="<@spring.message "form.placeholder.business" />">
                    </div>
                    <div class="mb-3">
                        <label for="features" class="form-label"><@spring.message "form.features" /></label>
                        <textarea class="form-control" id="features" name="features" rows="3"
                                  placeholder="<@spring.message "form.placeholder.features" />"></textarea>
                    </div>
                    <button type="button" id="createBtn" class="btn btn-primary"><@spring.message "common.confirm" /></button>
                </form>
            </div>

            <div class="details-container base-req">
                <h2 class="form-title"><@spring.message "form.title.requirements" /></h2>
                <div class="detail-item">
                    <span class="detail-label"><@spring.message "form.industry" />:</span>
                    <span class="detail-content industry"></span>
                </div>
                <div class="detail-item">
                    <span class="detail-label"><@spring.message "form.pageType" />:</span>
                    <span class="detail-content pageType"></span>
                </div>
                <div class="detail-item">
                    <span class="detail-label"><@spring.message "form.business" />:</span>
                    <span class="detail-content business"></span>
                </div>
                <div class="detail-item">
                    <span class="detail-label"><@spring.message "form.features" />:</span>
                    <span class="detail-content features"></span>
                </div>
            </div>


            <div class="ai-tip-analysis">
                <div class="chat-message ">
                    <div class="icon-container">
                        <i class="bi bi-robot"></i>
                    </div>
                    <div class="message-content">
                        <@spring.message "common.planning" />
                    </div>
                </div>
            </div>

            <div class="details-container page-analysis">
                <h2 class="form-title"><@spring.message "form.title.plan" /></h2>
                <div class="detail-item scroll-y" style="max-height: 300px">
                </div>

            </div>


            <div class="ai-tip-coding">
                <div class="chat-message ">
                    <div class="icon-container">
                        <i class="bi bi-robot"></i>
                    </div>
                    <div class="message-content">
                        <@spring.message "common.coding" />
                        <div class="output">
                        </div>
                    </div>
                </div>
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
                Page Preview
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
                        <div class="form-text">
                            Only stored in your browser. Never stored on server
                            <@spring.message "common.savetip" />
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