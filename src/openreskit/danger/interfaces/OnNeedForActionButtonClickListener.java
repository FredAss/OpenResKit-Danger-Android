package openreskit.danger.interfaces;

import java.util.List;
import openreskit.danger.models.QuestionWrapper;
import openreskit.danger.models.Threat;

public interface OnNeedForActionButtonClickListener {
	public void OnNeedForActionClicked(Threat threat, List<QuestionWrapper> question);
	
}
