package ru.modulkassa.payment.library.ui

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.modulkassa.payment.library.DefaultSettingsRepository
import ru.modulkassa.payment.library.R
import ru.modulkassa.payment.library.databinding.FragmentPaymentBinding
import ru.modulkassa.payment.library.domain.PaymentTerminalImpl
import ru.modulkassa.payment.library.domain.entity.PaymentOptions
import ru.modulkassa.payment.library.domain.entity.position.Position
import ru.modulkassa.payment.library.domain.entity.result.PaymentResultSuccess
import ru.modulkassa.payment.library.network.GsonFactory
import ru.modulkassa.payment.library.network.NetworkModule
import java.math.BigDecimal

internal class PaymentBottomSheetFragment : BottomSheetDialogFragment(), PaymentView {

    companion object {
        const val TAG = "paymentBottomSheetFragment"

        fun create(): PaymentBottomSheetFragment {
            return PaymentBottomSheetFragment()
        }
    }

    private var presenter: PaymentPresenter? = null

    private val browserActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            presenter?.getPaymentResult(paymentOptions.orderId)
        }

    private lateinit var paymentOptions: PaymentOptions

    private var binding: FragmentPaymentBinding? = null

    private lateinit var inventPositionAdapter: InventPositionAdapter

    override fun getTheme(): Int = R.style.Theme_Modul_BottomSheetDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = PaymentPresenter(
            PaymentTerminalImpl(
                api = NetworkModule.payApi,
                gson = GsonFactory.provide(),
                repository = DefaultSettingsRepository(requireContext())
            )
        )
        presenter?.attachView(this)

        val action = requireActivity().intent.getStringExtra(PaymentActivity.KEY_ACTION)
        if (action == PaymentActivity.CREATE_PAYMENT_ACTION) {
            val options = requireActivity().intent.extras?.let { PaymentOptions.fromBundle(it) }
            if (options != null) {
                paymentOptions = options

                presenter?.checkPaymentOptionsAndShow(paymentOptions)

                binding?.payBySbp?.setOnClickListener {
                    presenter?.payBySbp(paymentOptions)
                }
            } else {
                setErrorResult(NoPaymentOptionsErrorResult())
            }
        } else if (action == PaymentActivity.GET_PAYMENT_RESULT_ACTION) {
            val orderId = requireActivity().intent.getStringExtra(PaymentActivity.KEY_ORDER_ID)
            if (orderId.isNullOrBlank()) {
                setErrorResult(NoOrderIdErrorResult())
            } else {
                presenter?.getPaymentResult(orderId)
            }
        }
    }

    override fun onDestroyView() {
        presenter?.detachView()
        super.onDestroyView()
        binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        setErrorResult(CanceledByUserErrorResult())
    }

    override fun setErrorResult(error: BaseErrorResult) {
        val result = error.toPaymentResultError(requireContext())
        requireActivity().setResult(Activity.RESULT_CANCELED, Intent().apply { putExtras(result.toBundle()) })
        requireActivity().finish()
    }

    override fun setSuccessResult(result: PaymentResultSuccess) {
        requireActivity().setResult(Activity.RESULT_OK, Intent().apply { putExtras(result.toBundle()) })
        requireActivity().finish()
    }

    override fun showDescription(description: String) {
        binding?.description?.text = description
    }

    override fun showPositions(positions: List<Position>) {
        binding?.positions?.visibility = View.VISIBLE
        binding?.positions?.layoutManager = LinearLayoutManager(context)
        binding?.positions?.addItemDecoration(
            RecyclerViewMaterialDivider(requireContext(), LinearLayoutManager.VERTICAL)
        )
        inventPositionAdapter = InventPositionAdapter(positions)
        binding?.positions?.adapter = inventPositionAdapter

        (dialog as? BottomSheetDialog)?.behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun showSum(sum: BigDecimal) {
        binding?.summary?.text = RubSuffixSumFormatter().format(sum)
    }

    override fun showProgress(@StringRes progressResource: Int) {
        binding?.descriptionLayout?.visibility = View.INVISIBLE
        binding?.progressLayout?.visibility = View.VISIBLE
        binding?.progressTitle?.text = getString(progressResource)
    }

    override fun hideProgress() {
        binding?.progressLayout?.visibility = View.GONE
    }

    override fun sendSbpLink(sbpLink: String) {
        try {
            println("Переходим на оплату в банковское приложение")
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(sbpLink))
            browserActivityResultLauncher.launch(browserIntent)
        } catch (exception: NullPointerException) {
            setErrorResult(NoPaymentAppErrorResult())
        } catch (exception: ActivityNotFoundException) {
            setErrorResult(NoPaymentAppErrorResult())
        }
    }
}