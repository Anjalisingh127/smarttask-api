# AI-assisted development record

This repository was scaffolded with AI assistance. Treat the suggestions as draft code and verify them with tests and manual API runs. The record below describes implementation choices, not a claim that every verification has completed.

| Task | AI suggestion | Human review point | Verification |
| --- | --- | --- | --- |
| Component boundaries | Separate controller, service, repository, factory | Confirm service owns operations and controller owns HTTP | Read architecture; run service tests |
| Persistence | Use JPA repository and enum strings | Confirm combined filters and timestamp lifecycle | Run `mvn verify`; manual MySQL run |
| Input handling | Validate request DTOs centrally | Check blank titles and malformed enum values | MockMvc 400 tests |
| Error response | Use a common status/message/timestamp body | Check missing ID maps to 404 | MockMvc 404 test |
| Documentation | Draft README and API examples | Follow instructions on a fresh machine | Fresh-clone run remains to be done |

Add dated, specific accepted, changed, and rejected suggestions as you work on the repository. Do not claim human decisions or tests you have not personally performed.
