package com.metalichesky.zvuk.generator.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.metalichesky.zvuk.generator.R
import com.metalichesky.zvuk.generator.view.adapter.WaveformAdapter
import com.metalichesky.zvuk.generator.viewmodel.SoundGeneratorViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.metalichesky.zvuk.audio.Volume
import com.metalichesky.zvuk.generator.App
import com.metalichesky.zvuk.generator.Constants
import com.metalichesky.zvuk.generator.view.adapter.AlphabetAdapter
import com.metalichesky.zvuk.generator.view.adapter.BaseAdapter
import com.metalichesky.zvuk.generator.view.adapter.NoiseAdapter
import com.metalichesky.zvuk.generator.listitem.AlphabetItem
import com.metalichesky.zvuk.generator.listitem.BaseItem
import com.metalichesky.zvuk.generator.listitem.NoiseTypeItem
import com.metalichesky.zvuk.generator.listitem.WaveformTypeItem
import com.metalichesky.zvuk.generator.databinding.FragmentSoundGeneratorBinding
import com.metalichesky.zvuk.generator.di.ViewModelFactory
import com.metalichesky.zvuk.generator.model.PlaybackState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import javax.inject.Inject

class SoundGeneratorFragment : Fragment() {
    companion object {
        const val LOG_TAG = "SoundGeneratorFragment"
        const val VOLUME_MAX = 1000
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    val generatorViewModel: SoundGeneratorViewModel by activityViewModels{ viewModelFactory }

    private var binding: FragmentSoundGeneratorBinding? = null

    private var waveformAdapter: WaveformAdapter? = null
    private var noiseAdapter: NoiseAdapter? = null
    private var alphabetAdapter: AlphabetAdapter? = null

    private val baseAdapterListener = object: BaseAdapter.AdapterListener {
        override fun onClicked(item: BaseItem, position: Int) {
            when(item) {
                is AlphabetItem -> generatorViewModel.setAlphabet(item.alphabet)
                is WaveformTypeItem -> generatorViewModel.setWaveformType(item.waveformType)
                is NoiseTypeItem -> generatorViewModel.setNoiseType(item.noiseType)
            }
        }
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(LOG_TAG, "Exception in coroutine", throwable)
    }

    private fun <T> Flow<T>.inLifecycle() = flowOn(Dispatchers.Default + coroutineExceptionHandler)
        .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)

    private fun <T> Flow<T>.launchInLifecycleScope() = launchIn(lifecycleScope + coroutineExceptionHandler)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSoundGeneratorBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()

        generatorViewModel.waveformTypeItemsFlow.inLifecycle().onEach(::showWaveformTypes).launchInLifecycleScope()
        generatorViewModel.noiseTypeItemsFlow.inLifecycle().onEach(::showNoiseTypes).launchInLifecycleScope()
        generatorViewModel.playing.inLifecycle().onEach(::showIsPlaying).launchInLifecycleScope()
        generatorViewModel.frequency.inLifecycle().onEach(::showFrequency).launchInLifecycleScope()
        generatorViewModel.volume.inLifecycle().onEach( ::showVolume).launchInLifecycleScope()
        generatorViewModel.noiseVolume.inLifecycle().onEach(::showNoiseVolume).launchInLifecycleScope()
        generatorViewModel.groupsPerMinute.inLifecycle().onEach(::showGroupsPerMinute).launchInLifecycleScope()
        generatorViewModel.textSize.inLifecycle().onEach(::showTextSize).launchInLifecycleScope()
        generatorViewModel.alphabetFlow.inLifecycle().onEach(::showAlphabets).launchInLifecycleScope()
    }


    private fun setupViews() {
        binding?.apply {
            waveformAdapter = WaveformAdapter(baseAdapterListener)
            rvWaveformType.adapter = waveformAdapter
            val waveformLayoutManager = FlexboxLayoutManager(context)
            waveformLayoutManager.flexDirection = FlexDirection.ROW
            waveformLayoutManager.justifyContent = JustifyContent.CENTER
            rvWaveformType.layoutManager = waveformLayoutManager

            noiseAdapter = NoiseAdapter(baseAdapterListener)
            rvNoiseType.adapter = noiseAdapter
            val noiseLayoutManager = FlexboxLayoutManager(context)
            noiseLayoutManager.flexDirection = FlexDirection.ROW
            noiseLayoutManager.justifyContent = JustifyContent.CENTER
            rvNoiseType.layoutManager = noiseLayoutManager

            alphabetAdapter = AlphabetAdapter(baseAdapterListener)
            rvAlphabet.adapter = alphabetAdapter
            val alphabetLayoutManager = FlexboxLayoutManager(context)
            alphabetLayoutManager.flexDirection = FlexDirection.ROW
            alphabetLayoutManager.justifyContent = JustifyContent.CENTER
            rvAlphabet.layoutManager = alphabetLayoutManager

            pbGeneratorPlayback.setOnClickListener {
                if (generatorViewModel.playing.value == true) {
                    generatorViewModel.pause()
                } else {
                    generatorViewModel.play()
                }
            }
            sbFrequency.max = Constants.MAX_FREQUENCY
            sbVolume.max = VOLUME_MAX
            sbNoiseVolume.max = VOLUME_MAX
            sbGroupsPerMinute.max = Constants.MAX_SPEED
            sbTextSize.max = Constants.MAX_TEXT_SIZE

            sbFrequency.progress = generatorViewModel.getFrequency().toInt()
            sbVolume.progress =
                (generatorViewModel.getVolume().getDb() * (VOLUME_MAX / Volume.VOLUME_MAX_DB)).toInt()
            sbNoiseVolume.progress =
                (generatorViewModel.getNoiseVolume().getDb() * (VOLUME_MAX / Volume.VOLUME_MAX_DB)).toInt()
            sbGroupsPerMinute.progress = (generatorViewModel.getGroupsPerMinute().toInt())
            sbTextSize.progress = (generatorViewModel.getTextSize())

            sbFrequency.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    generatorViewModel.setFrequency(progress.toFloat())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val frequency = seekBar?.progress ?: return
                    if (frequency < Constants.MIN_FREQUENCY) seekBar.progress = Constants.MIN_FREQUENCY
                }
            })
            sbVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    generatorViewModel.setVolume(Volume.fromDb(progress * (Volume.VOLUME_MAX_DB / VOLUME_MAX)))
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
            sbNoiseVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    generatorViewModel.setNoiseVolume(Volume.fromDb(progress * (Volume.VOLUME_MAX_DB / VOLUME_MAX)))
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
            sbGroupsPerMinute.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    generatorViewModel.setGroupsPerMinute(progress.toFloat())
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val groupsPerMinute = seekBar?.progress ?: return
                    if (groupsPerMinute < Constants.MIN_SPEED) seekBar.progress = Constants.MIN_SPEED
                }
            })
            sbTextSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    generatorViewModel.setTextSize(progress)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    val groups = seekBar?.progress ?: return
                    if (groups < Constants.MIN_TEXT_SIZE) seekBar.progress = Constants.MIN_TEXT_SIZE
                }
            })
        }
    }

    private fun showWaveformTypes(waveformTypes: List<WaveformTypeItem>?) {
        waveformTypes ?: return
        waveformAdapter?.items = waveformTypes
    }

    private fun showNoiseTypes(noiseTypes: List<NoiseTypeItem>?) {
        noiseTypes ?: return
        noiseAdapter?.items = noiseTypes
    }

    private fun showIsPlaying(isPlaying: Boolean?) {
        isPlaying ?: return
        binding?.apply {
            if (isPlaying) {
                pbGeneratorPlayback.setState(PlaybackState.STOP)
            } else {
                pbGeneratorPlayback.setState(PlaybackState.PLAY)
            }
        }
    }

    private fun showFrequency(frequency: Float?) {
        frequency ?: return
        binding?.apply {
            val frequencyNotation = String.format("%.2f", frequency)
            tvFrequency.text = getString(R.string.frequency, frequencyNotation)
        }
    }

    private fun showVolume(volume: Volume?) {
        volume ?: return
        binding?.apply {
            val volumeNotation = String.format("%.2f", volume.getDb())
            tvVolume.text = getString(R.string.volume, volumeNotation)
        }
    }


    private fun showNoiseVolume(noiseVolume: Volume?) {
        noiseVolume ?: return
        binding?.apply {
            val volumeNotation = String.format("%.2f", noiseVolume.getDb())
            tvNoiseVolume.text = getString(R.string.noise_volume, volumeNotation)
        }
    }

    private fun showGroupsPerMinute(groupsPerMinute: Float?) {
        groupsPerMinute ?: return
        binding?.apply {
            val gpm = String.format("%.2f", groupsPerMinute)
            tvSpeed.text = getString(R.string.morse_speed, gpm)
        }
    }

    private fun showTextSize(groups: Int?) {
        groups ?: return
        binding?.apply {
            val g = String.format("%d", groups)
            tvTextSize.text = getString(R.string.morse_text_size, g)
        }
    }

    private fun showAlphabets(alphabets: List<AlphabetItem>?) {
        alphabets ?: return
        alphabetAdapter?.items = alphabets
    }

    override fun onStart() {
        super.onStart()
        generatorViewModel.start()
    }

    override fun onStop() {
        super.onStop()
        generatorViewModel.stop()
    }

}