class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/" {
            controller = "alternativeLoci"
            action = "index"
        }
        "500"(view:'/error')
	}
}
