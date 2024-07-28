package com.julia.imp.common.pdf

import platform.UIKit.UIDocumentInteractionController
import platform.UIKit.UIDocumentInteractionControllerDelegateProtocol
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.darwin.NSObject

class PdfViewerDelegate(
    private val rootController: UIViewController
) : NSObject(), UIDocumentInteractionControllerDelegateProtocol {

    override fun documentInteractionControllerViewForPreview(controller: UIDocumentInteractionController): UIView {
        return rootController.view
    }

    override fun documentInteractionControllerViewControllerForPreview(controller: UIDocumentInteractionController): UIViewController {
        return rootController
    }
}