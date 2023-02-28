package ru.modulkassa.payment.library.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.databinding.FragmentPaymentBinding
import ru.modulkassa.payment.library.entity.PaymentOptions

internal class PaymentBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        private const val KEY_PAYMENT_OPTIONS = "key_payment_options"
        const val TAG = "paymentBottomSheetFragment"

        fun create(options: PaymentOptions): PaymentBottomSheetFragment {
            val arguments = Bundle()
            arguments.putBundle(KEY_PAYMENT_OPTIONS, options.toBundle())

            val fragment = PaymentBottomSheetFragment()
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun getTheme(): Int = R.style.Theme_Modul_BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    private var binding: FragmentPaymentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.finish()
        // todo обрабатывать через setResult(Activity.RESULT_CANCELED, ..)
    }
}