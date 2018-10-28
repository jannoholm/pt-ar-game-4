/// @description Initial spawner settings

// Global parameter to track "age" of the soldier
global.soldierSpawnIndex = 0;

// STATE MACHINE START
enum SpawnerPhase {
	INIT, // 
	PREPARE_NEXT_WAVE, // Choose lanes and add indicators
	WAIT_FOR_WAVE, // Wait till next wave trigger
	TRIGGER_WAVE, // Single wave activities done
	WAVES_COMPLETE // No more waves
}

phases = ds_list_create();
ds_list_add(phases, 
	"INIT",
	"PREPARE_NEXT_WAVE",
	"WAIT_FOR_WAVE",
	"TRIGGER_WAVE",
	"WAVES_COMPLETE"
)

previousPhase = SpawnerPhase.INIT;
currentPhase = SpawnerPhase.INIT;
// STATE MACHINE END

enum SpawnerWave {
	WAVE_01,
	WAVE_02,
	WAVE_03,
	WAVE_04,
	WAVE_05,
	WAVE_06,
	WAVE_07,
	WAVE_08,
	WAVE_09,
	WAVE_10,
	WAVE_11,
	WAVE_12,
	WAVE_13,
	WAVE_14
}

nextWave = SpawnerWave.WAVE_01;
nextWaveTasks = ds_list_create();
nextWaveAlarmIndex = 0;