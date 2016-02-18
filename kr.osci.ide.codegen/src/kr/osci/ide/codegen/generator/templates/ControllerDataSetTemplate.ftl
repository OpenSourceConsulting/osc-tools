/**
 * ${dataSetClass} 수정
 */
@RequestMapping(value = "/Userupdate${dataSetLower}", method = RequestMethod.POST)
@ResponseBody
public void update${dataSetClass}(@RequestBody ${dataSetClass} param) throws Exception
{
	Service.update${dataSetClass}(param);
}