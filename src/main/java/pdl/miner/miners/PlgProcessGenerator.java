package pdl.miner.miners;

import java.io.File;
import java.util.UUID;

import pdl.miners.IMiner;
import pdl.types.TypeBPMN;
import pdl.utils.RepositoryUtils;
import pdl.web.models.Miner;
import pdl.web.models.MinerConfiguration;
import plg.generator.process.ProcessGenerator;
import plg.generator.process.RandomizationConfiguration;
import plg.io.exporter.BPMNExporter;
import plg.model.Process;

public class PlgProcessGenerator implements IMiner {

	public static final String id = UUID.randomUUID().toString();

	@Override
	public Miner getMinerWebModel() {
		Miner m = new Miner();
		m.setId(id);
		m.setName("PLG - Process Generator");
		m.addOutput(TypeBPMN.instance.getResourceType());
		return m;
	}

	@Override
	public void mine(MinerConfiguration configuration) {
		plg.model.Process process = new Process("PLG generated process");
		ProcessGenerator.randomizeProcess(process, RandomizationConfiguration.BASIC_VALUES);

		try {
			File tmpFile = File.createTempFile("result-of-bpmn", ".bpmn");
			BPMNExporter exporter = new BPMNExporter();
			exporter.exportModel(process, tmpFile.getAbsolutePath());

			RepositoryUtils.storeResource(tmpFile, configuration.getRepositoryHostname());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
